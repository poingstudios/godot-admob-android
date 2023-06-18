package com.poingstudios.godot.admob.ads

import android.util.ArraySet
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.poingstudios.godot.admob.ads.converters.convertToAdRequest
import com.poingstudios.godot.admob.ads.converters.convertToGodotDictionary
import com.poingstudios.godot.admob.core.utils.LogUtils
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class PoingGodotAdMobRewardedAd(godot: Godot?) : org.godotengine.godot.plugin.GodotPlugin(godot) {
    private val rewardedAds = mutableListOf<RewardedAd?>()
    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        val signals: MutableSet<SignalInfo> = ArraySet()
        signals.add(SignalInfo("on_rewarded_ad_failed_to_load", Integer::class.java, Dictionary::class.java))
        signals.add(SignalInfo("on_rewarded_ad_loaded", Integer::class.java))

        signals.add(SignalInfo("on_rewarded_ad_clicked", Integer::class.java))
        signals.add(SignalInfo("on_rewarded_ad_dismissed_full_screen_content", Integer::class.java))
        signals.add(SignalInfo("on_rewarded_ad_failed_to_show_full_screen_content", Integer::class.java, Dictionary::class.java))
        signals.add(SignalInfo("on_rewarded_ad_impression", Integer::class.java))
        signals.add(SignalInfo("on_rewarded_ad_showed_full_screen_content", Integer::class.java))

        signals.add(SignalInfo("on_rewarded_ad_user_earned_reward", Integer::class.java, Dictionary::class.java))
        return signals
    }

    @UsedByGodot
    fun create() : Int{
        val uid = rewardedAds.size
        rewardedAds.add(null)
        return uid
    }

    @UsedByGodot
    fun load(adUnitId : String, adRequestDictionary : Dictionary, keywords : Array<String>, uid: Int){
        activity!!.runOnUiThread{
            LogUtils.debug("loading rewarded ad")
            val adRequest = adRequestDictionary.convertToAdRequest(keywords)

            RewardedAd.load(activity!!,
                adUnitId, adRequest, object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        emitSignal("on_rewarded_ad_failed_to_load", loadAdError.convertToGodotDictionary())
                    }
                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        rewardedAds[uid] = rewardedAd
                        rewardedAd.fullScreenContentCallback = object: FullScreenContentCallback() {
                            override fun onAdClicked() {
                                LogUtils.debug("Ad was clicked.")
                                emitSignal("on_rewarded_ad_clicked", uid)
                            }

                            override fun onAdDismissedFullScreenContent() {
                                LogUtils.debug("Ad dismissed fullscreen content.")
                                rewardedAds[uid] = null
                                emitSignal("on_rewarded_ad_dismissed_full_screen_content", uid)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                LogUtils.debug("Ad failed to show fullscreen content.")
                                rewardedAds[uid] = null
                                emitSignal("on_rewarded_ad_failed_to_show_full_screen_content", uid, adError.convertToGodotDictionary())
                            }

                            override fun onAdImpression() {
                                LogUtils.debug("Ad recorded an impression.")
                                emitSignal("on_rewarded_ad_impression", uid)
                            }

                            override fun onAdShowedFullScreenContent() {
                                LogUtils.debug("Ad showed fullscreen content.")
                                emitSignal("on_rewarded_ad_showed_full_screen_content", uid)
                            }
                        }
                        emitSignal("on_rewarded_ad_loaded", uid)
                    }
                }
            )
        }
    }

    @UsedByGodot
    fun show(uid : Int){
        activity!!.runOnUiThread {
            rewardedAds[uid]?.show(activity!!)
            {
                emitSignal("on_rewarded_ad_user_earned_reward", uid, it.convertToGodotDictionary())
                LogUtils.debug("User earned the reward.")
            }
        }
    }
}