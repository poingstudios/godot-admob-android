def can_build(env, plat):
	return plat=="iphone"

def configure(env):
	if env['platform'] == 'iphone':
		env.AppendUnique(FRAMEWORKPATH=['#modules/admob/lib/GoogleMobileAds.xcframework/ios-armv7_arm64'], )
		env.Append(CPPPATH=['#core'])
