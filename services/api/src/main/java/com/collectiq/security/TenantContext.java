package com.collectiq.security;
public final class TenantContext{private static final ThreadLocal<String>T=new ThreadLocal<>();private TenantContext(){}public static void set(String v){T.set(v);}public static String get(){String x=T.get();if(x==null)throw new IllegalStateException("No authenticated tenant context");return x;}public static void clear(){T.remove();}}
