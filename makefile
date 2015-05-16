JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        BinaryConversion.java \
        FibonacciHeap.java \
        TrieNode.java \
        Trie.java\
        ssp.java\
        routing.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class