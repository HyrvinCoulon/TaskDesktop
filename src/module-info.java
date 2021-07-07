module Tasked {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires mail;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.client5.httpclient5.win;
    requires commons.cli;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.core5.httpcore5.h2;
    requires org.apache.httpcomponents.client5.httpclient5.fluent;
    requires org.apache.httpcomponents.client5.httpclient5.cache;
    requires org.reactivestreams;
    requires org.apache.commons.codec;
    requires org.apache.httpcomponents.client5.httpclient5.testing;
    requires org.apache.httpcomponents.core5.httpcore5.testing;
    requires com.sun.jna;
    //requires org.slf4j;
    //requires slf4j.api;
    //requires org.slf4j.nop;
    requires io.reactivex.rxjava2;
    requires activation;
    opens sample;
}