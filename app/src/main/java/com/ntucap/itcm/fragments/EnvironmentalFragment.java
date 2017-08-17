package com.ntucap.itcm.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.InvalidBandVersionException;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandAltimeterEvent;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.BandAmbientLightEvent;
import com.microsoft.band.sensors.BandAmbientLightEventListener;
import com.microsoft.band.sensors.BandBarometerEvent;
import com.microsoft.band.sensors.BandBarometerEventListener;
import com.microsoft.band.sensors.BandCaloriesEvent;
import com.microsoft.band.sensors.BandCaloriesEventListener;
import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEvent;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandGyroscopeEvent;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEvent;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.BandUVEvent;
import com.microsoft.band.sensors.BandUVEventListener;
import com.microsoft.band.sensors.GsrSampleRate;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.R;
import com.ntucap.itcm.classes.ITCMBandData;
import com.ntucap.itcm.classes.events.BandConnectEvent;
import com.ntucap.itcm.classes.events.PickerShowEvent;
import com.ntucap.itcm.utils.EventUtil;
import com.ntucap.itcm.views.RoundProgressDisplayView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import javax.xml.transform.Result;

/**
 * Created by ProgrammerYuan on 18/04/17.
 */

public class EnvironmentalFragment extends ITCMFragment {

    private BandClient client = null;
    private String TEMPLATE_HEARTRATE;
    private String TEMPLATE_CALORIES;
    private String TEMPLATE_UV_LEVEL;
    private String TEMPLATE_SKIN_TEMPERATURE;
    private String TEMPLATE_DISTANCE;
    private String TEMPLATE_AIR_TEMPERATURE;
    private String TEMPLATE_HUMIDITY;
    private String TEMPLATE_AIR_PRESSURE;

    private TextView mTvHeartRate, mTvCalories, mTvUVLevel, mTvSkinTemp, mTvDistance, mTvAirTemp,
            mTvHumidity, mTvAirPressure;
    private RoundProgressDisplayView mHeartRateView, mCaloriesView, mUVLevelView, mSkinTempView,
            mDistanceView, mAirTempView, mHumidityView, mAirPressView;

    private static final float MAX_HUMIDITY_VALUE = 90;
    private static final float MAX_HEARTRATE_VALUE = 180;
    private static final float MAX_AIR_TEMPERATURE_VALUE = 45;
    private static final float MAX_SKIN_TEMPERATURE_VALUE = 45;

    private BandHeartRateEventListener mHeartRateListener = new BandHeartRateEventListener() {
        @Override
        public void onBandHeartRateChanged(final BandHeartRateEvent event) {
            if (event != null && mInitialized) {
                getBandData().setHeartRate(event.getHeartRate());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvHeartRate.setText(String.format(TEMPLATE_HEARTRATE, event.getHeartRate()));
                        mHeartRateView.setValues(
                                String.valueOf(event.getHeartRate()),
                                event.getHeartRate() * 100f / MAX_HEARTRATE_VALUE
                        );
                    }
                });
            }
        }
    };

    private BandCaloriesEventListener mCaloriesListener = new BandCaloriesEventListener() {
        @Override
        public void onBandCaloriesChanged(final BandCaloriesEvent event) {
            if (event != null && mInitialized) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getBandData().setCaloryToday(event.getCaloriesToday());
                            mTvCalories.setText(String.format(TEMPLATE_CALORIES, event.getCaloriesToday()));
                        } catch (InvalidBandVersionException e) {
                            mTvCalories.setText("Not Supported");
                        }
                    }
                });
            }
        }
    };

    private BandUVEventListener mUVListener = new BandUVEventListener() {
        @Override
        public void onBandUVChanged(final BandUVEvent event) {
            if (event != null && mInitialized) {
                getBandData().setUVIndexLevel(event.getUVIndexLevel());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvUVLevel.setText(String.format(TEMPLATE_UV_LEVEL, event.getUVIndexLevel()));
                    }
                });
            }
        }
    };

    private BandSkinTemperatureEventListener mSkinTemperatureListener
            = new BandSkinTemperatureEventListener() {
        @Override
        public void onBandSkinTemperatureChanged(final BandSkinTemperatureEvent event) {
            if (event != null && mInitialized) {
                getBandData().setSkinTemp(event.getTemperature());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvSkinTemp.setText(String.format(TEMPLATE_SKIN_TEMPERATURE, event.getTemperature()));
                        mSkinTempView.setValues(
                                String.valueOf(event.getTemperature()),
                                event.getTemperature() * 100f / MAX_SKIN_TEMPERATURE_VALUE
                        );
                    }
                });
            }
        }
    };

    private BandDistanceEventListener mDistanceListener = new BandDistanceEventListener() {
        @Override
        public void onBandDistanceChanged(final BandDistanceEvent event) {
            if (event != null && mInitialized) {
                getBandData().setMotionType(event.getMotionType());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getBandData().setCurrentPace(event.getPace());
                            getBandData().setCurrentSpeed(event.getSpeed());
                            getBandData().setDistanceToday(event.getDistanceToday() / 100);
                            mTvDistance.setText(String.format(TEMPLATE_DISTANCE, event.getDistanceToday() / 100));
                        } catch (InvalidBandVersionException e) {
                            mTvDistance.setText(String.format(TEMPLATE_DISTANCE, 0));
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };

    private BandBarometerEventListener mBarometerListener = new BandBarometerEventListener() {
        @Override
        public void onBandBarometerChanged(final BandBarometerEvent event) {
            if (event != null && mInitialized) {
                getBandData().setAirPressure(event.getAirPressure());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("BAND_LOG: ", String.valueOf(event.getAirPressure()));
                        mTvAirPressure.setText(String.format(TEMPLATE_AIR_PRESSURE, event.getAirPressure()));
                        mTvAirTemp.setText(String.format(TEMPLATE_AIR_TEMPERATURE, event.getTemperature()));
                        mAirTempView.setValues(
                                String.valueOf(event.getTemperature()),
                                (float) event.getTemperature() * 100f / MAX_AIR_TEMPERATURE_VALUE
                        );
                    }
                });
            }
        }
    };
    private BandAmbientLightEventListener mAmbientLightListener = new BandAmbientLightEventListener() {

        @Override
        public void onBandAmbientLightChanged(BandAmbientLightEvent event) {
            getBandData().setAmbientLight(event.getBrightness());
        }
    };
    private BandAltimeterEventListener mAltimeterListener = new BandAltimeterEventListener() {
        @Override
        public void onBandAltimeterChanged(BandAltimeterEvent event) {
            try {
                getBandData().setFlightsAscended(event.getFlightsAscendedToday());
                getBandData().setTotalGain(event.getTotalGainToday());
            } catch (InvalidBandVersionException e) {
                e.printStackTrace();
            }
            getBandData().setFlightsDesended(event.getFlightsDescended());
            getBandData().setSteppingGain(event.getSteppingGain());
            getBandData().setSteppingLoss(event.getSteppingLoss());
            getBandData().setStepsAscended(event.getStepsAscended());
            getBandData().setStepsDescended(event.getStepsDescended());
            getBandData().setTotalLoss(event.getTotalLoss());
            getBandData().setRate(event.getRate());
        }
    };

    private BandGyroscopeEventListener mGyrocopeListener = new BandGyroscopeEventListener() {
        @Override
        public void onBandGyroscopeChanged(BandGyroscopeEvent event) {
            getBandData().setAccelerationX(event.getAccelerationX());
            getBandData().setAccelerationY(event.getAccelerationY());
            getBandData().setAccelerationZ(event.getAccelerationZ());
            getBandData().setAngularVelocityX(event.getAngularVelocityX());
            getBandData().setAngularVelocityY(event.getAngularVelocityY());
            getBandData().setAngularVelocityZ(event.getAngularVelocityZ());
        }
    };

    private BandGsrEventListener mGsrListener = new BandGsrEventListener() {
        @Override
        public void onBandGsrChanged(BandGsrEvent event) {
            getBandData().setGSR(event.getResistance());
        }
    };

    private BandRRIntervalEventListener mRRIntervalListener = new BandRRIntervalEventListener() {
        @Override
        public void onBandRRIntervalChanged(BandRRIntervalEvent event) {
            getBandData().setRRInterval(event.getInterval());
        }
    };

    private BandPedometerEventListener mPedometerListener = new BandPedometerEventListener() {
        @Override
        public void onBandPedometerChanged(BandPedometerEvent event) {
            try {
                getBandData().setStepsToday(event.getStepsToday());
            } catch (InvalidBandVersionException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentId = EventUtil.SENDER_ID_ENVIRONMENTAL;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState,
                R.layout.fragment_environmental);

        if (!mInitialized) {
            mTvAirTemp = (TextView) mInflatedView.findViewById(R.id.tv_airtemp_frag_env);
            mTvCalories = (TextView) mInflatedView.findViewById(R.id.tv_calories_frag_env);
            mTvDistance = (TextView) mInflatedView.findViewById(R.id.tv_distance_frag_env);
            mTvHeartRate = (TextView) mInflatedView.findViewById(R.id.tv_heartrate_frag_env);
            mTvSkinTemp = (TextView) mInflatedView.findViewById(R.id.tv_skintemp_frag_env);
            mTvUVLevel = (TextView) mInflatedView.findViewById(R.id.tv_uv_frag_env);
            mTvHumidity = (TextView) mInflatedView.findViewById(R.id.tv_humidity_frag_env);
            mTvAirPressure = (TextView) mInflatedView.findViewById(R.id.tv_airpressure_frag_env);

            mAirTempView = (RoundProgressDisplayView)
                    mInflatedView.findViewById(R.id.progressbar_airtemp_frag_env);
            mCaloriesView = (RoundProgressDisplayView)
                    mInflatedView.findViewById(R.id.progressbar_calories_frag_env);
            mDistanceView = (RoundProgressDisplayView)
                    mInflatedView.findViewById(R.id.progressbar_distance_frag_env);
            mHeartRateView = (RoundProgressDisplayView)
                    mInflatedView.findViewById(R.id.progressbar_heartrate_frag_env);
            mSkinTempView = (RoundProgressDisplayView)
                    mInflatedView.findViewById(R.id.progressbar_skintemp_frag_env);
            mUVLevelView = (RoundProgressDisplayView)
                    mInflatedView.findViewById(R.id.progressbar_uv_frag_env);
            mHumidityView = (RoundProgressDisplayView)
                    mInflatedView.findViewById(R.id.progressbar_humidity_frag_env);
            mAirPressView = (RoundProgressDisplayView)
                    mInflatedView.findViewById(R.id.progressbar_airpress_frag_env);

            mAirPressView.setProgress(100f);
            mDistanceView.setProgress(100f);
            mCaloriesView.setProgress(100f);
            mUVLevelView.setProgress(100f);

            Resources resources = mContext.getResources();

            TEMPLATE_AIR_TEMPERATURE = resources.getString(R.string.str_air_temp_template);
            TEMPLATE_CALORIES = resources.getString(R.string.str_calories_template);
            TEMPLATE_DISTANCE = resources.getString(R.string.str_distance_template);
            TEMPLATE_HEARTRATE = resources.getString(R.string.str_heart_rate_template);
            TEMPLATE_SKIN_TEMPERATURE = resources.getString(R.string.str_skin_temp_template);
            TEMPLATE_UV_LEVEL = resources.getString(R.string.str_uv_level_template);
            TEMPLATE_HUMIDITY = resources.getString(R.string.str_humidity_template);
            TEMPLATE_AIR_PRESSURE = resources.getString(R.string.str_air_pressure_template);

            mInitialized = true;
        }
        return mInflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();
        appendToUI("FRAGMENT ONSTART!!!!!!!!!");
        final WeakReference<Activity> reference = new WeakReference<>((Activity) mContext);
        new HeartRateConsentTask().execute(reference);
    }

    @Override
    public void onStop() {
        super.onStop();
        appendToUI("FRAGMENT ONSTOP!!!!!!!!!");
        new DataUnsubscriptionTask().execute();
    }

    private class DataSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {

                if (getConnectedBandClient()) {
                    if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                        client.getSensorManager().registerHeartRateEventListener(mHeartRateListener);
                    } else {
                        appendToUI("You have not given this application consent to access heart rate data yet."
                                + " Please press the Heart Rate Consent button.\n");
                    }
                    client.getSensorManager().registerUVEventListener(mUVListener);
                    client.getSensorManager().registerCaloriesEventListener(mCaloriesListener);
                    client.getSensorManager().registerDistanceEventListener(mDistanceListener);
                    client.getSensorManager().registerBarometerEventListener(mBarometerListener);
                    client.getSensorManager().registerAltimeterEventListener(mAltimeterListener);
                    client.getSensorManager().registerPedometerEventListener(mPedometerListener);
                    client.getSensorManager().registerRRIntervalEventListener(mRRIntervalListener);
                    client.getSensorManager().registerAmbientLightEventListener(mAmbientLightListener);
                    client.getSensorManager().registerGsrEventListener(mGsrListener, GsrSampleRate.MS200);
                    client.getSensorManager().registerSkinTemperatureEventListener(mSkinTemperatureListener);
                    client.getSensorManager().registerGyroscopeEventListener(mGyrocopeListener, SampleRate.MS32);

                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }

    private class DataUnsubscriptionTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    client.getSensorManager().unregisterHeartRateEventListeners();
                    client.getSensorManager().unregisterBarometerEventListeners();
                    client.getSensorManager().unregisterDistanceEventListeners();
                    client.getSensorManager().unregisterSkinTemperatureEventListeners();
                    client.getSensorManager().unregisterUVEventListeners();
                    client.getSensorManager().unregisterAmbientLightEventListener(mAmbientLightListener);
                    client.getSensorManager().unregisterAltimeterEventListener(mAltimeterListener);
                    client.getSensorManager().unregisterGsrEventListener(mGsrListener);
                    client.getSensorManager().unregisterGyroscopeEventListener(mGyrocopeListener);
                    client.getSensorManager().unregisterRRIntervalEventListener(mRRIntervalListener);
                    client.getSensorManager().unregisterPedometerEventListener(mPedometerListener);
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }

    private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Boolean> {

        private boolean mConsentGiven;

        @Override
        protected Boolean doInBackground(WeakReference<Activity>... params) {
            try {
                if (getConnectedBandClient()) {
                    if (params[0].get() != null) {
                        client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                            @Override
                            public void userAccepted(boolean consentGiven) {
                                new DataSubscriptionTask().execute();
                            }
                        });
                    }
                    return true;
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return false;
        }
    }

    private void appendToUI(String msg) {
        Log.d("BAND LOG:", msg);
    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.\n");

                return false;
            }
            client = BandClientManager.getInstance().create(getActivity(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        appendToUI("Band is connecting...\n");
        return ConnectionState.CONNECTED == client.connect().await();
    }
}
