package gs.momokun.tabtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by ElmoTan on 10/21/2016.
 */

public class TwoFragment extends Fragment {
    View v;
    Button btn_about,pair_button;
    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Called","onCreate2");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_two, container, false);

        btn_about = (Button) v.findViewById(R.id.about_button);
        pair_button = (Button) v.findViewById(R.id.pair_button);

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(v.getContext(),"Home Automation Nightly Build v0.0001",Toast.LENGTH_SHORT).show();
            }
        });

        pair_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PairDevice.class);
                startActivity(i);
            }
        });



        return v;
    }
}
