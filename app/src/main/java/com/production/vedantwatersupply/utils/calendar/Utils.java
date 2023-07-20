package com.production.vedantwatersupply.utils.calendar;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

public class Utils {
	public static final int PULSE_ANIMATOR_DURATION = 544;

	/**
	 * Render an animator to pulsate a view in place.
	 * 
	 * @param labelToAnimate
	 *            the view to pulsate.
	 * @return The animator object. Use .start() to begin.
	 */
	public static ObjectAnimator getPulseAnimator(View labelToAnimate,
                                                  float decreaseRatio, float increaseRatio) {
		Keyframe k0 = Keyframe.ofFloat(0f, 1f);
		Keyframe k1 = Keyframe.ofFloat(0.275f, decreaseRatio);
		Keyframe k2 = Keyframe.ofFloat(0.69f, increaseRatio);
		Keyframe k3 = Keyframe.ofFloat(1f, 1f);

		PropertyValuesHolder scaleX = PropertyValuesHolder.ofKeyframe("scaleX",
				k0, k1, k2, k3);
		PropertyValuesHolder scaleY = PropertyValuesHolder.ofKeyframe("scaleY",
				k0, k1, k2, k3);
		ObjectAnimator pulseAnimator = ObjectAnimator.ofPropertyValuesHolder(
				labelToAnimate, scaleX, scaleY);
		pulseAnimator.setDuration(PULSE_ANIMATOR_DURATION);

		return pulseAnimator;
	}
}
