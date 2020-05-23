def can_build(env, plat):
	return plat=="iphone"

def configure(env):
	if env['platform'] == 'iphone':
		env.Append(FRAMEWORKPATH=['#modules/admob/ios/lib'])
		env.Append(CPPPATH=['#core'])
		env.Append(LINKFLAGS=['-ObjC', '-framework','AdSupport', '-framework','CoreTelephony', '-framework','EventKit', '-framework','EventKitUI', '-framework','MessageUI', '-framework','StoreKit', '-framework','SafariServices', '-framework','CoreBluetooth', '-framework','AssetsLibrary', '-framework','CoreData', '-framework','CoreLocation', '-framework','CoreText', '-framework','ImageIO', '-framework', 'GLKit', '-framework','CoreVideo', '-framework', 'CFNetwork', '-framework', 'MobileCoreServices', '-framework', 'GoogleMobileAds'])
