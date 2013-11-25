/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

/**
 *
 * @author Clouder
 */
public class AddressUtility {

    private String ip,netmask;
    private String network,broadcast,gateway;

    public AddressUtility(String ip, String netmask) {
        this.ip = ip;
        this.netmask = netmask;
        String[] d = ip.split("\\.");
        String[] nm = netmask.split("\\.");
        if(d.length==4){
            short[] dir = new short[4];
            for(int e=0;e<4;e++)dir[e]=Short.parseShort(d[e]);
            short[] netm = new short[4];
            for(int e=0;e<4;e++)netm[e]=Short.parseShort(nm[e]);

            short[] network=new short[4],broadcast=new short[4],gateway=new short[4];
            for(int e=0;e<4;e++){
                gateway[e]=(short)(dir[e]&netm[e]);
                network[e]=(short)(dir[e]&netm[e]);
                broadcast[e]=(short)(((dir[e]&netm[e])|(-netm[e]-1))&0xFF);
            }
            gateway[3]++;
            this.network=""+network[0];
            this.broadcast=""+broadcast[0];
            this.gateway=""+gateway[0];
            for(int e=1;e<4;e++){
                this.network+="."+network[e];
                this.broadcast+="."+broadcast[e];
                this.gateway+="."+gateway[e];
            }
        }
    }

    public String getBroadcast() {
        return broadcast;
    }

    public String getGateway() {
        return gateway;
    }

    public String getIp() {
        return ip;
    }

    public String getNetmask() {
        return netmask;
    }

    public String getNetwork() {
        return network;
    }


}
