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
