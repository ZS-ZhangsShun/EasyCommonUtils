package com.zs.easy.common.http.retrofit;

import com.zs.easy.common.utils.LogUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Dns;

public class XDns implements Dns {
    private long timeout;
    public static boolean IS_PRINT_DNS_INFO = true;

    public XDns(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public List<InetAddress> lookup(final String hostname) throws UnknownHostException {
        if (hostname == null) {
            throw new UnknownHostException("hostname == null");
        } else {
            try {
                FutureTask<List<InetAddress>> task = new FutureTask<>(
                        new Callable<List<InetAddress>>() {
                            @Override
                            public List<InetAddress> call() throws Exception {
                                List<InetAddress> lookup = Dns.SYSTEM.lookup(hostname);
                                if (IS_PRINT_DNS_INFO) {
                                    for (int i = 0; i < lookup.size(); i++) {
                                        LogUtil.i("Address = " + lookup.get(i).getAddress());
                                        LogUtil.i("CanonicalHostName = " + lookup.get(i).getCanonicalHostName());
                                        LogUtil.i("HostAddress = " + lookup.get(i).getHostAddress());
                                        LogUtil.i("HostName = " + lookup.get(i).getHostName());
                                    }
                                }
                                return lookup;
                            }
                        });
                new Thread(task).start();
                return task.get(timeout, TimeUnit.SECONDS);
            } catch (Exception var4) {
                LogUtil.e("XDns lookup error " + var4.toString());
                UnknownHostException unknownHostException = new UnknownHostException("Broken system behaviour for dns lookup of " + hostname);
                unknownHostException.initCause(var4);
                throw unknownHostException;
            }
        }
    }
}
