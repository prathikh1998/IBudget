package com.example.ibudget.ui.graph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payment;
import com.example.ibudget.databinding.FragmentGraphBinding;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class GraphFragment extends Fragment {

    private FragmentGraphBinding binding;

    private static AppDatabase db;
    private static List<Payment> paymentData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(this.getContext());
        paymentData = db.paymentDao().getAll();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GraphViewModel graphViewModel = new ViewModelProvider(this).get(GraphViewModel.class);

        binding = FragmentGraphBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

//        final TextView textView = binding.textGraph;
//        graphViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        GraphView graph = (GraphView) binding.graph;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1000),
                new DataPoint(1, 980),
                new DataPoint(2, 860),
                new DataPoint(3, 850),
        });

        StaticLabelsFormatter statfmt = new StaticLabelsFormatter(graph);
        statfmt.setHorizontalLabels(new String[]{"5/21/23","5/27/23","5/31/23","6/1/23"});
        graph.getGridLabelRenderer().setLabelFormatter(statfmt);

        graph.addSeries(series);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}