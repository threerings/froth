//
// $Id$
//
// Utility functions used in the bindings.

#include "utils.h"

void setBufferLimit (JNIEnv* env, jobject buffer, uint32 limit)
{
    jclass bclazz = env->FindClass("java/nio/Buffer");
    jmethodID mid = env->GetMethodID(bclazz, "limit", "(I)Ljava/nio/Buffer;");
    env->CallObjectMethod(buffer, mid, limit);
}

uint32 getBufferLimit (JNIEnv* env, jobject buffer)
{
    jclass bclazz = env->FindClass("java/nio/Buffer");
    jmethodID mid = env->GetMethodID(bclazz, "limit", "()I");
    return env->CallIntMethod(buffer, mid);
}
