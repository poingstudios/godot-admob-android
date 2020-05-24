def can_build(env, plat):
	return plat=="iphone"

def configure(env):
	if env['platform'] == 'iphone':
		env.Append(FRAMEWORKPATH=['#modules/admob/ios/lib'])
		env.Append(CPPPATH=['#core'])
