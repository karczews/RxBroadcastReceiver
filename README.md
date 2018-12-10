# ReactiveBroadcast
Simple RxJava2 binding for Android BroadcastReceiver

[![Build Status](https://travis-ci.org/karczews/RxBroadcastReceiver.svg?branch=master)](https://travis-ci.org/karczews/RxBroadcastReceiver)
[![codecov](https://codecov.io/gh/karczews/RxBroadcastReceiver/branch/master/graph/badge.svg)](https://codecov.io/gh/karczews/RxBroadcastReceiver)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.karczews/rx2-broadcast-receiver.svg?style=flat)](https://repo.maven.apache.org/maven2/com/github/karczews/rx2-broadcast-receiver/) 
[![Nexus Snapshots](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.github.karczews/rx2-broadcast-receiver.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/github/karczews/rx2-broadcast-receiver/)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/karczews/RxBroadcastReceiver/blob/master/LICENSE) <a href="http://www.methodscount.com/?lib=com.github.karczews%3Arx2-broadcast-receiver%3A1.0.3"><img src="https://img.shields.io/badge/Methods and size-core: 30 | deps: 9458 | 6 KB-e91e63.svg"/></a></a>


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


What's new:
- 1.0.2 library utilizes [Context#registerReceiver(BroadcastReceiver, filter, broadcastPermission, scheduler)](https://goo.gl/ytDVGb) method when subscription occurs on background thread looper.
- 1.0.5 fixed problem with manifest merger.

Download
--------

To use library with Gradle

```groovy
dependencies {
  implementation 'com.github.karczews:rx2-broadcast-receiver:1.0.5'
}
```

or using Maven:

```xml
<dependency>
    <groupId>com.github.karczews</groupId>
    <artifactId>rx2-broadcast-receiver</artifactId>
    <version>1.0.5</version>
</dependency>
```
