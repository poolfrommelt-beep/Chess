#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_grandmasteredge_engine_stockfish_StockfishBridge_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from Stockfish Bridge";
    return env->NewStringUTF(hello.c_str());
}
