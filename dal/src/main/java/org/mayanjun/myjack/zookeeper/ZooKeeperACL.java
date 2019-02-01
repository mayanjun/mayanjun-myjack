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

package org.mayanjun.myjack.zookeeper;

/**
 * ZooKeeperAuthInfo
 *
 * @author mayanjun(6/12/16)
 */
public class ZooKeeperACL {

    public ZooKeeperACL() {
    }

    public ZooKeeperACL(String scheme, String auth) {
        this.scheme = scheme;
        this.auth = auth;
        this.authBytes = auth.getBytes();
    }

    public ZooKeeperACL(String scheme, byte[] authBytes) {
        this.scheme = scheme;
        this.authBytes = authBytes;
        this.auth = new String(authBytes);
    }

    private String scheme;

    private String auth;

    private byte authBytes[];

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
        this.authBytes = auth.getBytes();
    }

    public byte[] getAuthBytes() {
        return authBytes;
    }

    public void setAuthBytes(byte[] authBytes) {
        this.authBytes = authBytes;
        this.auth = new String(authBytes);
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
