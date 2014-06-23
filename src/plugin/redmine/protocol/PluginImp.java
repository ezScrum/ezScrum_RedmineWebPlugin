package plugin.redmine.protocol;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.ui.protocol.EzScrumUI;
import ntut.csie.ui.protocol.PluginUI;
import ntut.csie.ui.protocol.ProductBacklogUI;
import ntut.csie.ui.protocol.UIConfig;

public class PluginImp extends UIConfig{

	@Override
	public void setEzScrumUIList(List<EzScrumUI> ezScrumUIList) {
		final PluginUI pluginUI = new PluginUI(){
			public String getPluginID(){
				return "redminePlugin";
			}
		};
		
		ezScrumUIList.add( pluginUI );
		
		ProductBacklogUI productBacklogUI = new ProductBacklogUI(){

			@Override
			public List<String> getToolbarPluginIDList() {
				List<String> toolbarPluginIDList = new ArrayList<String>();
				toolbarPluginIDList.add("redmineBtnPlugin");
				return toolbarPluginIDList;
			}

			@Override
			public PluginUI getPluginUI() {
				// TODO Auto-generated method stub
				return pluginUI;
			}
 
			
		};
		
		ezScrumUIList.add( productBacklogUI );
	}

}
