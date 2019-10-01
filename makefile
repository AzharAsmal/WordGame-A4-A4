# A makefile

JC = javac
JFLAGS = -g
BINDIR=./bin
SRCDIR=./src/com/company
DOCDIR=./javadocs

.SUFFIXES: .java .class

${BINDIR}/%.class: ${SRCDIR}/%.java
	javac $< -cp ${BINDIR} -d ${BINDIR}

${BINDIR}/WordApp.class: ${BINDIR}/Score.class ${BINDIR}/WordDictionary.class ${BINDIR}/WordRecord.class ${BINDIR}/skeletonSlide.class ${BINDIR}/WordPanel.class ${SRCDIR}/WordApp.java


clean:
	rm -f ${BINDIR}/*.class

docs:
	javadoc  -classpath ${BINDIR} -d ${DOCDIR} ${SRCDIR}/*.java

cleandocs:
	rm -rf ${DOCDIR}/*
