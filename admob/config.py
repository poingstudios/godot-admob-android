def can_build(env, plat):
	return plat=="android" or platform=="iphone"

def configure(env):
	if env['platform'] == 'android':
		env.android_add_java_dir("android/java")
		env.android_add_res_dir("android/res")
		#3.1.2 will only use 17.2.1, because on > 18.0.0, play-services-ads uses androidX, and 3.1.2 wasnt build for it
		env.android_add_dependency("compile ('com.google.android.gms:play-services-ads:17.2.1') { exclude group: 'com.android.support' }") #exclude group due: Android导入多个第三方库导致 Duplicate class的问题排查思路: https://muka.app/?p=270
		env.android_add_to_manifest("android/manifests/AndroidManifest.xml")
		#initial config (https://developers.google.com/admob/android/quick-start)
	elif env['platform'] == 'iphone':
		pass