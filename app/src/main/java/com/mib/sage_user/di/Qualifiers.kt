package com.mib.sage_user.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApiConfigQualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class AccessTokenInterceptorQualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class BaseUrlQualifier