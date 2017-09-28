package de.iosb.fraunhofer.baeconroomreservation.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.fragments.PickUserFragment;

/**
 *
 * @author Viselsav Sako
 */
public class PickUserView extends LinearLayout {

    private String username;
    private PickUserFragment pickUserFragment;

    View rootView;
    ImageView close;
    TextView name;

    public PickUserView(PickUserFragment pickUserFragment) {
        super(pickUserFragment.getContext());
        this.pickUserFragment = pickUserFragment;
        init(pickUserFragment.getContext());
    }

    public PickUserView(Context context) {
        super(context);
        init(context);
    }

    public PickUserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickUserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PickUserView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void init(Context context)
    {
        rootView = inflate(context, R.layout.userview, this);
        name = (TextView) rootView.findViewById(R.id.name);
        close = (ImageView) rootView.findViewById(R.id.close);
        final LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        params.setMargins(10,10,10,10);
        rootView.setLayoutParams(params);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pickUserFragment.deleteUser(username);
                ViewGroup parent = (ViewGroup) rootView.getParent();
                parent.removeView(rootView);
            }
        });
    }

    public void setName(String name)
    {
        this.name.setText(name);
    }
}
