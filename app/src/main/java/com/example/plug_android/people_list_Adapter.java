package com.example.plug_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class people_list_Adapter extends RecyclerView.Adapter<people_list_Adapter.ViewHolder> {

    private User[] user_data;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profile_pic;
        private final ImageView call;
        private final TextView friend_name;
        private final TextView friend_email;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            friend_email = (TextView) view.findViewById(R.id.friend_email);
            friend_name = (TextView) view.findViewById(R.id.friend_name);
            profile_pic = (ImageView) view.findViewById(R.id.people);
            call = (ImageView) view.findViewById(R.id.makecall);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeCall();
                }
            });
        }

        private void makeCall() {
            //TODO: Decide how call is implemented after studying webrtc
        }
        private ImageView getProfilePic(){return profile_pic;}
        private TextView getFriend_name(){return friend_name;}
        private TextView getFriend_email(){return friend_email;}
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param users String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public people_list_Adapter(User[] users) {
        user_data = users.clone();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.available_user_fragment, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getFriend_email().setText(user_data[position].getEmail());
        viewHolder.getFriend_name().setText(user_data[position].getDisplayName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return user_data.length;
    }
}

