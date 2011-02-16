//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamApps.h"

JNIEXPORT jstring JNICALL Java_com_threerings_froth_SteamApps_getCurrentGameLanguage (
    JNIEnv* env, jclass clazz)
{
    return env->NewStringUTF(SteamApps()->GetCurrentGameLanguage());
}
