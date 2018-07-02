package com.zm.goods.utils;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpDelete extends HttpEntityEnclosingRequestBase{

	public HttpDelete()
    {
    }

    public HttpDelete(URI uri)
    {
        setURI(uri);
    }

    public HttpDelete(String uri)
    {
        setURI(URI.create(uri));
    }

    public String getMethod()
    {
        return METHOD_NAME;
    }

    public static final String METHOD_NAME = "DELETE";

}
