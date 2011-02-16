//
// $Id$

#include <steam_api.h>

#include "com_threerings_froth_SteamUser.h"

JNIEXPORT jlong JNICALL Java_com_threerings_froth_SteamUser_getSteamID (
    JNIEnv* env, jclass clazz)
{
    return SteamUser()->GetSteamID().ConvertToUint64();
}
