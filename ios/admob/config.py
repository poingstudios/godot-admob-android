def can_build(*argv):
	platform = argv[1] if len(argv) == 2 else argv[0]
	return platform=="iphone"

def configure(env):
	if env['platform'] == 'iphone':
		xcframework_directory = ''
		if env['arch'] == 'x86_64':
			xcframework_directory = 'ios-x86_64-simulator'
		else:
			xcframework_directory = 'ios-armv7_arm64'

		env.Append(FRAMEWORKPATH=['#modules/admob/lib/GoogleMobileAds.xcframework/' + xcframework_directory])
		env.Append(CPPPATH=['#core'])
