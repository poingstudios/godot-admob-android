def can_build(env, plat):
	return plat=="android" 

def configure(env):
	if env['platform'] == 'android':
		env.android_add_java_dir("src")