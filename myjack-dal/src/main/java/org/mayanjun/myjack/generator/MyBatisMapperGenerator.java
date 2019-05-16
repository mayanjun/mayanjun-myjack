/*
 * Copyright 2016-2018 mayanjun.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mayanjun.myjack.generator;

import org.mayanjun.myjack.util.SqlUtils;

import java.util.List;

/**
 * Generate mapper xml file for MyBatis
 *
 * @author mayanjun(8/19/15)
 * @since 0.0.1
 */
public class MyBatisMapperGenerator {

    private String quote = "`";

    public void setQuote(String quote) {
        this.quote = quote;
    }

    /**
     * Generate mapper xml file for MyBatis
     * @param clazz
     * @return
     */
    public String generate(Class<?> clazz) {
        StringBuffer stringBuffer = new StringBuffer();

        List<AnnotationHolder> holders = AnnotationHelper.getAnnotationHolders(clazz);

        if (holders == null || holders.isEmpty()) throw new IllegalArgumentException("No @Column found");

        AnnotationHolder primaryHolder = null;
        for (AnnotationHolder ah : holders) if (ah.getPrimaryKey() != null) primaryHolder = ah;

        if (primaryHolder == null) throw new IllegalArgumentException("No primary key found");

        handleHeader(stringBuffer, clazz);
        handleCommonSQL(stringBuffer, clazz, holders);
        handleDELETE(stringBuffer, clazz, primaryHolder);
        handleGET(stringBuffer, clazz);
        handleLIST(stringBuffer, clazz);
        handleINSERT(stringBuffer, clazz, holders);
        handleUPDATE(stringBuffer, clazz, holders, primaryHolder);
        handleTAIL(stringBuffer, clazz);

        return stringBuffer.toString();
    }

    protected void handleHeader(StringBuffer stringBuffer, Class<?> clazz) {
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n")
                .append("<!DOCTYPE mapper PUBLIC \"-//dao.org//DTD Mapper 3.0//EN\" \r\n")
                .append("\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n")
                .append("\r\n<mapper namespace=\"")
                .append(clazz.getSimpleName())
                .append("Mapper\">");

    }

    protected void handleCommonSQL(StringBuffer stringBuffer, Class<?> clazz, List<AnnotationHolder> holders) {
        stringBuffer.append("\r\n\t<sql id=\"tableName\">" + quote + AnnotationHelper.getTableName(clazz) + quote + "</sql>")
                .append("\r\n\t<sql id=\"allColumns\">")
                .append(calcFieldList(holders))
                .append("</sql>");
    }

    private String calcFieldList(List<AnnotationHolder> holders) {
        StringBuffer sb = new StringBuffer();
        if (holders != null && !holders.isEmpty()) {
            int size = holders.size();
            for (int i = 0; i < size; i++) {
                AnnotationHolder ah = holders.get(i);
                sb.append(quote + AnnotationHelper.getColumnName(ah) + quote);
                if (ah.getOgnl() != null) {
                    String as = ah.getOgnl() + "." + ah.getField().getName();
                    sb.append(" AS " + quote + as + quote);
                }
                if(!SqlUtils.isBlank(ah.getColumn().referenceField())) {
                    String as = ah.getField().getName() + "." +  ah.getColumn().referenceField();
                    sb.append(" AS " + quote + as + quote);
                }
                if (i < size - 1) sb.append(",");
            }
        }
        return sb.toString();
    }

    protected void handleGET(StringBuffer stringBuffer, Class<?> clazz) {
        stringBuffer.append("\r\n\r\n")
                .append("\t<select id=\"get\" parameterType=\"long\" resultType=\"")
                .append(clazz.getCanonicalName())
                .append("\">")
                .append("\r\n\t\tselect <include refid=\"allColumns\"/> from <include refid=\"tableName\"/>")
                .append("\r\n\t\twhere id=#{_parameter}")
                .append("\r\n\t</select>");
    }

    protected void handleLIST(StringBuffer stringBuffer, Class<?> clazz) {
        stringBuffer.append("\r\n\r\n")
                .append("\t<select id=\"list\" parameterType=\"string\" resultType=\"")
                .append(clazz.getCanonicalName())
                .append("\">")
                .append("\r\n\t\tselect <include refid=\"allColumns\"/> from <include refid=\"tableName\"/>")
                .append("\r\n\t\t<where>${_parameter}</where>")
                .append("\r\n\t</select>");
    }

    protected void handleINSERT(StringBuffer stringBuffer, Class<?> clazz, List<AnnotationHolder> holders) {
        stringBuffer.append("\r\n\r\n")
                .append("\t<insert id=\"insert\" parameterType=\"")
                .append(clazz.getCanonicalName())
                .append("\">")
                .append("\r\n\t\tinsert into <include refid=\"tableName\"/>(")
                .append("\r\n\t\t<trim suffixOverrides=\",\">")
                .append(calcInsertColumns(holders, "\t\t\t"))
                .append("\r\n\t\t</trim>")
                .append("\r\n\t\t)")
                .append("\r\n\t\tvalues(")
                .append("\r\n\t\t<trim suffixOverrides=\",\">")
                .append(calcInsertColumnValues(holders, "\t\t\t"))
                .append("\r\n\t\t</trim>")
                .append("\r\n\t\t)")
                .append("\r\n\t</insert>");
    }

    protected String calcInsertColumnValues(List<AnnotationHolder> holders, String prefix) {
        StringBuffer sb = new StringBuffer();
        if (holders != null && !holders.isEmpty()) {
            for (AnnotationHolder ah : holders) {
                String columnName = AnnotationHelper.getColumnName(ah);
                sb.append("\r\n" + prefix)
                        .append("<if test=\"")
                        .append(
                                calcOgnlTest(ah.getOgnl() == null ? (
                                        ah.getField().getName() + (SqlUtils.isBlank(ah.getColumn().referenceField()) ? "" : "." + ah.getColumn().referenceField())
                                        ) : ah.getOgnl() + "." + ah.getField().getName())
                        )
                        .append("\">")
                        .append("#{" + (ah.getOgnl() == null ? (
                                SqlUtils.isBlank(ah.getColumn().referenceField()) ? ah.getField().getName() : ah.getField().getName() + "." + ah.getColumn().referenceField()
                                ) : ah.getOgnl() + "." + ah.getField().getName()) + "}")
                        .append(",</if>");
            }
        }
        return sb.toString();
    }

    protected String calcInsertColumns(List<AnnotationHolder> holders, String prefix) {
        StringBuffer sb = new StringBuffer();
        if (holders != null && !holders.isEmpty()) {
            for (AnnotationHolder ah : holders) {
                String columnName = AnnotationHelper.getColumnName(ah);
                sb.append("\r\n" + prefix)
                        .append("<if test=\"")
                        .append(
                                calcOgnlTest(ah.getOgnl() == null ? (
                                        ah.getField().getName() + (SqlUtils.isBlank(ah.getColumn().referenceField()) ? "" : "." + ah.getColumn().referenceField())
                                ) : ah.getOgnl() + "." + ah.getField().getName())
                        )
                        .append("\">" + quote)
                        .append(columnName)
                        .append(quote)
                        .append(",</if>");
            }
        }
        return sb.toString();
    }

    protected String calcOgnlTest(String ognl) {
        StringBuffer sb = new StringBuffer();
        String vvv = "";
        if (!SqlUtils.isBlank(ognl)) {
            String vls[] = ognl.split("\\.");
            for (int i = 0; i < vls.length; i++) {
                String v = vls[i];
                if (i > 0) {
                    sb.append(" and ");
                    vvv += ".";
                }
                String var = vvv + v;
                vvv = var;
                sb.append(var + " != null");
            }
        }
        return sb.toString();
    }

    protected String calcUpdateColumns(List<AnnotationHolder> holders, String prefix) {
        StringBuffer sb = new StringBuffer();
        if (holders != null && !holders.isEmpty()) {
            for (AnnotationHolder ah : holders) {
                String columnName = AnnotationHelper.getColumnName(ah);
                sb.append("\r\n" + prefix)
                        .append("<if test=\"")
                        .append(
                                calcOgnlTest(ah.getOgnl() == null ? (
                                        ah.getField().getName() + (SqlUtils.isBlank(ah.getColumn().referenceField()) ? "" : "." + ah.getColumn().referenceField())
                                        ) : ah.getOgnl() + "." + ah.getField().getName())
                        )
                        .append("\">" + quote)
                        .append(columnName)
                        .append(quote)
                        .append("=")
                        .append("#{" + (ah.getOgnl() == null ? (
                                ah.getField().getName() + (SqlUtils.isBlank(ah.getColumn().referenceField()) ? "" : "." + ah.getColumn().referenceField())
                                ) : ah.getOgnl() + "." + ah.getField().getName()) + "}")
                        .append(",</if>");
            }
        }
        return sb.toString();
    }

    protected void handleUPDATE(StringBuffer stringBuffer, Class<?> clazz,
                                List<AnnotationHolder> holders,
                                AnnotationHolder primaryHolder) {
        stringBuffer.append("\r\n\r\n")
                .append("\t<update id=\"update\" parameterType=\"")
                .append(clazz.getCanonicalName())
                .append("\">")
                .append("\r\n\t\tupdate <include refid=\"tableName\"/>")
                .append("\r\n\t\t<set>")
                .append(calcUpdateColumns(holders, "\t\t\t"))
                .append("\r\n\t\t</set>")
                .append("\r\n\t\t" + calcWherePrimaryKey(primaryHolder))
                .append("\r\n\t</update>");
    }

    protected void handleDELETE(StringBuffer stringBuffer,
                                Class<?> clazz,
                                AnnotationHolder primaryHolder) {
        stringBuffer.append("\r\n")
                .append("\r\n\t")
                .append("<delete id=\"delete\" parameterType=\"long\">")
                .append("\r\n\t\tdelete from <include refid=\"tableName\"/>")
                .append("\r\n\t\t" + calcWherePrimaryKey(primaryHolder))
                .append("\r\n\t</delete>");

    }

    protected String calcWherePrimaryKey(AnnotationHolder primaryHolder) {
        String colName = AnnotationHelper.getColumnName(primaryHolder);
        String var = primaryHolder.getField().getName();
        if (primaryHolder.getOgnl() != null) var = primaryHolder.getOgnl() + "." + var;
        return "<where>" + colName + "=#{" + var + "}</where>";
    }

    protected void handleTAIL(StringBuffer stringBuffer, Class<?> clazz) {
        stringBuffer.append("\r\n</mapper>");
    }
}
