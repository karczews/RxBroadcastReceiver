# ReactiveBroadcast
Simple RxJava2 binding for Android BroadcastReceiver

[![Build Status](https://travis-ci.org/karczews/RxBroadcastReceiver.svg?branch=master)](https://travis-ci.org/karczews/RxBroadcastReceiver)
[![codecov](https://codecov.io/gh/karczews/RxBroadcastReceiver/branch/master/graph/badge.svg)](https://codecov.io/gh/karczews/RxBroadcastReceiver)
![Maven Central](https://img.shields.io/maven-central/v/com.github.karczews/rx2-broadcast-receiver.svg?style=flat) 
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/karczews/RxBroadcastReceiver/blob/master/LICENSE)


Usage
--------
```java
RxBroadcastReceivers.fromIntentFilter(context, intentFilter)
        .subscribe(new Consumer<Intent>() {
          @Override public void accept(Intent broadcast) {
            // do something with broadcast
          }
        });
```

Download
--------

To use library with Gradle

```groovy
dependencies {
  compile 'com.github.karczews:rx2-broadcast-receiver:1.0.0'
}
```

or using Maven:

```xml
<dependency>
    <groupId>com.github.karczews</groupId>
    <artifactId>rx2-broadcast-receiver</artifactId>
    <version>1.0.0</version>
</dependency>
```
