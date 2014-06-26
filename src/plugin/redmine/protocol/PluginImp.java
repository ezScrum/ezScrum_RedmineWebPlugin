package plugin.redmine.protocol;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.ui.protocol.EzScrumUI;
import ntut.csie.ui.protocol.PluginUI;
import ntut.csie.ui.protocol.ProductBacklogUI;
import ntut.csie.ui.protocol.ProjectUI;
import ntut.csie.ui.protocol.UIConfig;

public class PluginImp extends UIConfig {

	@Override
	public void setEzScrumUIList(List<EzScrumUI> ezScrumUIList) {
		final PluginUI pluginUI = new PluginUI() {
			public String getPluginID() {
				return "redminePlugin";
			}
		};

		ezScrumUIList.add(pluginUI);

		ProductBacklogUI productBacklogUI = new ProductBacklogUI() {

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

		ezScrumUIList.add(productBacklogUI);

		/**
		 * add ReleasePlanUI to ezScrumUIList for ReleasePlan Pages view
		 */
		ProjectUI projectUI = new ProjectUI() {
			// 定義要放在左方Tree的node
			@Override
			public List<String> getProjectLeftTreeIDList() {
				List<String> projectLeftTreeIDList = new ArrayList<String>();
				projectLeftTreeIDList.add("redmine_TreeNode");
				return projectLeftTreeIDList;
			}

			// 定義要左方Tree的node要連結到的頁面
			@Override
			public List<String> getProjectPageIDList() {
				List<String> projectPageIDList = new ArrayList<String>();
				projectPageIDList.add("redmine_ConfigPage");
				return projectPageIDList;
			}

			@Override
			public PluginUI getPluginUI() {
				return pluginUI;
			}
		};
		ezScrumUIList.add(projectUI);
	}

}
