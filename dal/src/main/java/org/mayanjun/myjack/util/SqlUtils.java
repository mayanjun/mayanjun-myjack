package org.mayanjun.myjack.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Strings
 *
 * @author mayanjun(2/22/16)
 * @since 0.0.5
 */
public class SqlUtils {

    private static final Pattern HUMP_PATTERN = Pattern.compile("\\.(.)");

    public static String listToString(List<String> fields, String sep, String quote) {
        if(fields != null && !fields.isEmpty()) {
            int size = fields.size();
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < size; i++) {
                sb.append(quote + fields.get(i) + quote);
                if(i < size - 1) sb.append(sep);
            }
            return sb.toString();
        }
        return "";
    }

    public static String listToString(Collection<String> fields, String sep) {
        if(fields != null && !fields.isEmpty()) {
            int size = fields.size();
            String[]arr = new String[size];
            fields.toArray(arr);
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < size; i++) {
                sb.append(arr[i]);
                if(i < size - 1) sb.append(sep);
            }
            return sb.toString();
        }
        return "*";
    }

    public static String toHumpString(String src) {
        Matcher m = HUMP_PATTERN.matcher(src);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String find = m.group(1);
            m.appendReplacement(sb, find.toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String escapeSQLField(String src, boolean inReg) {
        if(src != null) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < src.length(); i++) {
                char c = src.charAt(i);
                if(inReg) {
                    if(c == '`') sb.append("\\\\\\");
                    if(c == '\\') sb.append("\\\\\\");
                }else {
                    if(c == '`') sb.append('\\');
                    if(c == '\\') sb.append('\\');
                }

                sb.append(c);
            }
            return sb.toString();
        }
        return "";
    }

    public static String escapeSQLParameter(String src, String charset, boolean inReg) {
        if(src == null) return null;
        int stringLength = src.length();

        String ec = "\\";
        if(inReg) ec = "\\\\\\";

        if (isEscapeNeededForString(src, stringLength)) {

            StringBuilder buf = new StringBuilder((int) (stringLength * 1.1));

            buf.append('\'');

            //
            // Note: buf.append(char) is _faster_ than appending in blocks, because the block append requires a System.arraycopy().... go figure...
            //

            for (int i = 0; i < stringLength; ++i) {
                char c = src.charAt(i);

                switch (c) {
                    case 0: /* Must be escaped for 'mysql' */
                        buf.append(ec);
                        buf.append('0');

                        break;

                    case '\n': /* Must be escaped for logs */
                        buf.append(ec);
                        buf.append('n');

                        break;

                    case '\r':
                        buf.append(ec);
                        buf.append('r');

                        break;

                    case '\\':
                        buf.append(ec);
                        buf.append('\\');

                        break;

                    case '\'':
                        buf.append(ec);
                        buf.append('\'');

                        break;

                    case '"': /* Better safe than sorry */
                        //if (this.usingAnsiMode) {
                        //   buf.append('\\');
                        // }

                        buf.append('"');

                        break;

                    case '\032': /* This gives problems on Win32 */
                        buf.append(ec);
                        buf.append('Z');

                        break;

                    case '\u00a5':
                    case '\u20a9':
                        // escape characters interpreted as backslash by mysql
                        CharsetEncoder encoder = Charset.forName(charset).newEncoder();
                        CharBuffer cbuf = CharBuffer.allocate(1);
                        ByteBuffer bbuf = ByteBuffer.allocate(1);
                        cbuf.put(c);
                        cbuf.position(0);
                        encoder.encode(cbuf, bbuf, true);
                        if (bbuf.get(0) == '\\') {
                            buf.append(ec);
                        }
                        // fall through

                    default:
                        buf.append(c);
                }
            }

            buf.append('\'');

            return buf.toString();
        }

        return "\'" + src + "\'";
    }

    private static boolean isEscapeNeededForString(String x, int stringLength) {
        boolean needsHexEscape = false;
        for (int i = 0; i < stringLength; ++i) {
            char c = x.charAt(i);

            switch (c) {
                case 0: /* Must be escaped for 'mysql' */

                    needsHexEscape = true;
                    break;

                case '\n': /* Must be escaped for logs */
                    needsHexEscape = true;

                    break;

                case '\r':
                    needsHexEscape = true;
                    break;

                case '\\':
                    needsHexEscape = true;

                    break;

                case '\'':
                    needsHexEscape = true;

                    break;

                case '"': /* Better safe than sorry */
                    needsHexEscape = true;

                    break;

                case '\032': /* This gives problems on Win32 */
                    needsHexEscape = true;
                    break;
            }

            if (needsHexEscape) {
                break; // no need to scan more
            }
        }
        return needsHexEscape;
    }
}
