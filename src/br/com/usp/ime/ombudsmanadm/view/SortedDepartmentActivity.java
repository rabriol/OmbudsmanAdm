package br.com.usp.ime.ombudsmanadm.view;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.usp.ime.ombudsmanadm.R;
import br.com.usp.ime.ombudsmanadm.model.vo.Incident;
import br.com.usp.ime.ombudsmanadm.task.IncidentSorterTask;
import br.com.usp.ime.ombudsmanadm.task.IncidentSorterTask.IncidentSorterCallBack;
import br.com.usp.ime.ombudsmanadm.view.adapter.SortedIncidentListAdapter;

public class SortedDepartmentActivity extends Activity implements IncidentSorterCallBack{
    
	private ListView incidentListView;
	private SortedIncidentListAdapter adapter;
	private Map<String, List<Incident>> sortedIncidents;
	private List<String> depts;
	
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_list);
        
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
        
        incidentListView = (ListView) findViewById(R.id.dept_list);
        
        incidentListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				List<Incident> incidents = sortedIncidents.get(depts.get(position));
				
				Intent intent = new Intent(SortedDepartmentActivity.this, SortedIncidentActivity.class);
				
				StringBuilder sb = new StringBuilder();
				boolean first = true;
				for (Incident incident : incidents) {
					if (first) {
						sb.append(incident.getId());
						first = false;
					} else {
						sb.append(", " + incident.getId());
					}
				}
				
				first = false;
				
				intent.putExtra("incidentsId", sb.toString());
				startActivity(intent);
			}
		});
        
        new IncidentSorterTask(this).execute();
    }
	
	@Override
	public void onIncidentSorterReturn(Map<String, List<Incident>> sortedIncidents, List<String> depts) {
		this.sortedIncidents = sortedIncidents;
		this.depts = depts;
		adapter = new SortedIncidentListAdapter(this, sortedIncidents, depts);
		incidentListView.setAdapter(adapter);
	}
}
