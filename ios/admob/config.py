def can_build(*argv):
	platform = argv[1] if len(argv) == 2 else argv[0]
	return platform=="iphone"

def configure(env):
	if env['platform'] == 'iphone':
		env.Append(FRAMEWORKPATH=['#modules/admob/lib/GoogleMobileAds.xcframework/ios-armv7_arm64'])
		env.Append(CPPPATH=['#core'])
