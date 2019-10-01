cmake_minimum_required(VERSION 2.8)
project(HelloWorld)

find_package(Java REQUIRED)
include(UseJava)

set(JAR_NAME WordApp.java)
set(JAVA_SOURCE_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR})
set(JAVA_SOURCE_FILES ${JAVA_SOURCE_DIRECTORY}/WordApp.java)
add_jar(${JAR_NAME} ${JAVA_SOURCE_FILES})
