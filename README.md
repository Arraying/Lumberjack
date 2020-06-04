# Lumberjack
A small async lightweight logging library.

## Premise

The purpose of Lumberjack is to be used alongside a proper logging framework such as SLF4J.
The logger, `LLogger`, is extremely flexible as it is an interface.
The default implementation also contains many configuration options.

## Routes

This section applies to using the default implementation only.
Routes, `LLoggerRoute`, are a set of ways something can be logged.
For example, to the standard output (STDOUT) or to a file.
Lumberjack makes defining routes very simple and easy.
Because routes are flexible, it's possible to route logs to a different logging library.

## Example
The following snippet will print all levels above and including INFO to STDOUT, and also printing errors to STDERR.
```java
LLogger logger = LLoggerBuilder.create("MyLogger")
    .withRouteStdOut(LLogLevel.INFO)
    .withRouteStdErr(LLogLevel.FATAL)
    .build();
logger.info("Hello %s!", "world");
```
It outputs the following.
```
[19:11:22] [main] INFO: Hello world!
```
Please note that the format is customisable.