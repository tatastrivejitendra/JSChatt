package com.tatastrive.jschatt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.getstream.sdk.chat.StreamChat;
import com.getstream.sdk.chat.enums.FilterObject;
import com.getstream.sdk.chat.rest.User;
import com.getstream.sdk.chat.rest.core.Client;
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel;
import com.tatastrive.jschatt.databinding.ActivityMainBinding;

import java.util.HashMap;

import static com.getstream.sdk.chat.enums.Filters.and;
import static com.getstream.sdk.chat.enums.Filters.eq;
import static com.getstream.sdk.chat.enums.Filters.in;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CHANNEL_TYPE = "com.example.chattutorial.CHANNEL_TYPE";
    public static final String EXTRA_CHANNEL_ID = "com.example.chattutorial.CHANNEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // setup the client using the example API key
        // normal you would call init in your Application class and not the activity
        StreamChat.init("b67pax5b2wdq", this.getApplicationContext());
        Client client = StreamChat.getInstance(this.getApplication());
        HashMap<String, Object> extraData = new HashMap<>();
        extraData.put("name", "Paranoid Android");
        extraData.put("image", "https://bit.ly/2TIt8NR");
        User currentUser = new User("yellow-fire-9", extraData);
        // User token is typically provided by your server when the user authenticates
        client.setUser(currentUser, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoieWVsbG93LWZpcmUtOSJ9.BBea8K0p5ha7kRYrffnDd0WITMuVKfISnxzXMRxsCbk");

        // we're using data binding in this example
        ActivityMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        // most the business logic for chat is handled in the ChannelListViewModel view model
        ChannelListViewModel viewModel = ViewModelProviders.of(this).get(ChannelListViewModel.class);
        binding.setViewModel(viewModel);
        binding.channelList.setViewModel(viewModel, this);

        // query all channels of type messaging
        FilterObject filter = and(eq("type", "messaging"), in("members", "yellow-fire-9"));
        viewModel.setChannelFilter(filter);

        // click handlers for clicking a user avatar or channel
        binding.channelList.setOnChannelClickListener(channel -> {
            Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
            intent.putExtra(EXTRA_CHANNEL_TYPE, channel.getType());
            intent.putExtra(EXTRA_CHANNEL_ID, channel.getId());
            startActivity(intent);
            // open the channel activity
        });
        binding.channelList.setOnUserClickListener(user -> {
            // open your user profile
        });

    }
}
