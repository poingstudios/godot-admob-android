#region Copyright
// MIT License
//
// Copyright (c) 2024-present Poing Studios
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
#endregion

#if TOOLS

using System;
using Godot;

namespace PoingStudios.GodotAdMob;

[Tool]
public partial class AdMobAdsConfig : EditorExportPlugin
{
    private const string PLUGIN_NAME = "PoingGodotAdMobAds";

    // Dependency paths relative to the project's addons folder.
    private const string LIB_PATH_RELEASE = "google_admob/libs/poing-godot-admob-ads-v1.0.0-release.aar";
    private const string LIB_PATH_DEBUG = "google_admob/libs/poing-godot-admob-ads-v1.0.0-debug.aar";
    private const string DEPENDENCY_PATH_RELEASE = "google_admob/libs/poing-godot-admob-core-v1.0.1-release.aar";
    private const string DEPENDENCY_PATH_DEBUG = "google_admob/libs/poing-godot-admob-core-v1.0.1-debug.aar";
    private const string ADMOB_DEPENDENCY = "com.google.android.gms:play-services-ads:22.4.0";


    public override bool _SupportsPlatform(EditorExportPlatform platform)
    {
        if (platform is EditorExportPlatformAndroid)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public override string[] _GetAndroidLibraries(EditorExportPlatform platform, bool debug)
    {
        if (debug)
        {
            return new string[] { LIB_PATH_DEBUG, DEPENDENCY_PATH_DEBUG };
        }
        else
        {
            return new string[] { LIB_PATH_RELEASE, DEPENDENCY_PATH_RELEASE };
        }
    }
    public override string[] _GetAndroidDependencies(EditorExportPlatform platform, bool debug)
    {
        return new string[] { ADMOB_DEPENDENCY };
    }
    public override string _GetName()
    {
        return PLUGIN_NAME;
    }
}

#endif