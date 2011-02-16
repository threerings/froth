//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamUtils.h"

JNIEXPORT jint JNICALL Java_com_threerings_froth_SteamUtils_getAppID (
    JNIEnv* env, jclass clazz)
{
    return SteamUtils()->GetAppID();
}

