//
// Built on Wed Jan 24 20:13:12 CET 2018 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.INFO

def LOG_PATTERN = "%date %level [%thread] %logger{10} [%file:%line] %msg%n"
def LOG_DIR = "./logs"
appender("FILE", RollingFileAppender) {
  file = "${LOG_DIR}/log.log"
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "${LOG_DIR}/log.%d{yyyyMMdd}.log"
    maxHistory = 30
  }
  encoder(PatternLayoutEncoder) {
    pattern = "${LOG_PATTERN}"
  }
}
appender("STDOUT", ConsoleAppender) {
  target = "System.out"
  target = "System.err"
  encoder(PatternLayoutEncoder) {
    pattern = "${LOG_PATTERN}"
  }
}
// appender("STDERR", ConsoleAppender) {
//   target = "System.err"
//   encoder(PatternLayoutEncoder) {
//     pattern = "%date %level [%thread] %logger{10} [%file:%line] %msg%n"
//   }
// }
// root(INFO, ["FILE", "STDERR"])
root(trace, ["FILE"])
