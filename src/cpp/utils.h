//
// $Id$
//
// Utility functions used in the bindings.

#ifndef UTILS
#define UTILS

#include <jni.h>
#include <steamtypes.h>

/**
 * Sets the limit of a buffer object.
 */
void setBufferLimit (JNIEnv* env, jobject buffer, uint32 limit);

/**
 * Returns the limit of a buffer object.
 */
uint32 getBufferLimit (JNIEnv* env, jobject buffer);

#endif
