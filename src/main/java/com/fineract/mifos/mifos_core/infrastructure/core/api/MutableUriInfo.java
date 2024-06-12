package com.fineract.mifos.mifos_core.infrastructure.core.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MutableUriInfo implements UriInfo {
    private final UriInfo delegate;
    private final MultivaluedMap<String, String> additionalQueryParameters = new MultivaluedHashMap<>();

    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        return fillAdditionalQueryParameters(delegate.getQueryParameters());
    }

    @Override
    public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
        return fillAdditionalQueryParameters(delegate.getQueryParameters(decode));
    }

    private MultivaluedMap<String, String> fillAdditionalQueryParameters(MultivaluedMap<String, String> queryParameters) {
        MultivaluedMap<String, String> newQueryParameters = new MultivaluedHashMap<>(queryParameters);
        newQueryParameters.putAll(additionalQueryParameters);
        return newQueryParameters;
    }

    public void addAdditionalQueryParameter(String key, String value) {
        additionalQueryParameters.add(key, value);
    }

    public void putAdditionalQueryParameter(String key, List<String> values) {
        additionalQueryParameters.put(key, values);
    }

    @Override
    public String getPath() {
        return delegate.getPath();
    }

    @Override
    public String getPath(boolean decode) {
        return delegate.getPath(decode);
    }

    @Override
    public List <PathSegment> getPathSegments() {
        return delegate.getPathSegments();
    }

    @Override
    public List<PathSegment> getPathSegments(boolean decode) {
        return delegate.getPathSegments(decode);
    }

    @Override
    public URI getRequestUri() {
        return delegate.getRequestUri();
    }

    @Override
    public UriBuilder getRequestUriBuilder() {
        return delegate.getRequestUriBuilder();
    }

    @Override
    public URI getAbsolutePath() {
        return delegate.getAbsolutePath();
    }

    @Override
    public UriBuilder getAbsolutePathBuilder() {
        return delegate.getAbsolutePathBuilder();
    }

    @Override
    public URI getBaseUri() {
        return delegate.getBaseUri();
    }

    @Override
    public UriBuilder getBaseUriBuilder() {
        return delegate.getBaseUriBuilder();
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        return delegate.getPathParameters();
    }

    @Override
    public MultivaluedMap<String, String> getPathParameters(boolean decode) {
        return delegate.getPathParameters(decode);
    }

    @Override
    public List<String> getMatchedURIs() {
        return delegate.getMatchedURIs();
    }

    @Override
    public List<String> getMatchedURIs(boolean decode) {
        return delegate.getMatchedURIs(decode);
    }

    @Override
    public List<Object> getMatchedResources() {
        return delegate.getMatchedResources();
    }

    @Override
    public URI resolve(URI uri) {
        return delegate.resolve(uri);
    }

    @Override
    public URI relativize(URI uri) {
        return delegate.relativize(uri);
    }

}
