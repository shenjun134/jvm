import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
 
import com.tjy.util.JVMGCUtils;
import com.tjy.util.JVMInfoUtils;
import com.tjy.util.JVMMemoryUtils;
import com.tjy.util.JVMMemoryUtils.JVMMemoryUsage;
import com.tjy.util.JVMThreadUtils;
 
/**
 * 
 * 应用状态监控,包括应用类型,版本,所在的tomcat名以及数据库连接等信息(代码有删减)
 * 
 * 
 */
public class ApplicationMonitorTask extends AbstractMonitorTask<ApplicationMonitorMessage> {
 
	@Override
	protected ApplicationMonitorMessage doRun() {
		return this.createMessage();
	}
 
	private ApplicationMonitorMessage createMessage() {
		ApplicationMonitorMessage message = new ApplicationMonitorMessage();
		// APP
		message.setVersion(ErlangMonitorConfigManager.getConfig().getAppVersion());
		// JVM
		setJVMInfo(message);
		// DB
		setDBInfo(message);
		return message;
	}
 
	private void setJVMInfo(ApplicationMonitorMessage message) {
		try {
			message.setPid(Integer.parseInt(JVMInfoUtils.getPID()));
		} catch (Exception e) {
		}
		message.setJavaVersion(JVMInfoUtils.getJavaVersion());
		message.setRunTime(JVMInfoUtils.getJVMUpTimeMs());
		message.setLoadedClassCount(JVMInfoUtils.getJVMLoadedClassCount());
		message.setUnloadedClassCount(JVMInfoUtils.getJVMUnLoadedClassCount());
		JVMMemoryUsage heapMemoryUsage = JVMMemoryUtils.getHeapMemoryUsage();
		if (heapMemoryUsage != null) {
			message.setHeapTotal(heapMemoryUsage.getMax());
			message.setHeapUsed(heapMemoryUsage.getUsed());
			message.setHeapUsedPercent(heapMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage nonHeapMemoryUsage = JVMMemoryUtils.getNonHeapMemoryUsage();
		if (nonHeapMemoryUsage != null) {
			message.setNonHeapTotal(nonHeapMemoryUsage.getMax());
			message.setNonHeapUsed(nonHeapMemoryUsage.getUsed());
			message.setNonHeapUsedPercent(nonHeapMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage edenMemoryUsage = JVMMemoryUtils.getEdenSpaceMemoryUsage();
		if (edenMemoryUsage != null) {
			message.setEdenTotal(edenMemoryUsage.getMax());
			message.setEdenUsed(edenMemoryUsage.getUsed());
			message.setEdenUsedPercent(edenMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage edenPeakMemoryUsage = JVMMemoryUtils.getAndResetEdenSpaceMemoryPeakUsage();
		if (edenPeakMemoryUsage != null) {
			message.setEdenPeakUsedPercent(edenPeakMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage survivorMemoryUsage = JVMMemoryUtils.getSurvivorSpaceMemoryUsage();
		if (survivorMemoryUsage != null) {
			message.setSurvivorTotal(survivorMemoryUsage.getMax());
			message.setSurvivorUsed(survivorMemoryUsage.getUsed());
			message.setSurvivorUsedPercent(survivorMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage survivorPeakMemoryUsage = JVMMemoryUtils.getAndResetSurvivorSpaceMemoryPeakUsage();
		if (survivorPeakMemoryUsage != null) {
			message.setSurvivorPeakUsedPercent(survivorPeakMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage oldGenMemoryUsage = JVMMemoryUtils.getOldGenMemoryUsage();
		if (oldGenMemoryUsage != null) {
			message.setOldTotal(oldGenMemoryUsage.getMax());
			message.setOldUsed(oldGenMemoryUsage.getUsed());
			message.setOldUsedPercent(oldGenMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage oldGenPeakMemoryUsage = JVMMemoryUtils.getAndResetOldGenMemoryPeakUsage();
		if (oldGenPeakMemoryUsage != null) {
			message.setOldPeakUsedPercent(oldGenPeakMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage permGenMemoryUsage = JVMMemoryUtils.getPermGenMemoryUsage();
		if (permGenMemoryUsage != null) {
			message.setPermTotal(permGenMemoryUsage.getMax());
			message.setPermUsed(permGenMemoryUsage.getUsed());
			message.setPermUsedPercent(permGenMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage permGenPeakMemoryUsage = JVMMemoryUtils.getAndResetPermGenMemoryPeakUsage();
		if (permGenPeakMemoryUsage != null) {
			message.setPermPeakUsedPercent(permGenPeakMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage codeCacheGenMemoryUsage = JVMMemoryUtils.getCodeCacheMemoryUsage();
		if (codeCacheGenMemoryUsage != null) {
			message.setCodeCacheTotal(codeCacheGenMemoryUsage.getMax());
			message.setCodeCacheUsed(codeCacheGenMemoryUsage.getUsed());
			message.setCodeCacheUsedPercent(codeCacheGenMemoryUsage.getUsedPercent());
		}
		JVMMemoryUsage codeCacheGenPeakMemoryUsage = JVMMemoryUtils.getAndResetCodeCacheMemoryPeakUsage();
		if (codeCacheGenPeakMemoryUsage != null) {
			message.setCodeCachePeakUsedPercent(codeCacheGenPeakMemoryUsage.getUsedPercent());
		}
 
		message.setYgcName(JVMGCUtils.getYoungGCName());
		message.setYgc(JVMGCUtils.getYoungGCCollectionCount());
		message.setYgcTime(JVMGCUtils.getYoungGCCollectionTime());
		message.setFgcName(JVMGCUtils.getFullGCName());
		message.setFgc(JVMGCUtils.getFullGCCollectionCount());
		message.setFgcTime(JVMGCUtils.getFullGCCollectionTime());
 
		message.setThreadCount(JVMThreadUtils.getThreadCount());
		message.setThreadPeakCount(JVMThreadUtils.getAndResetPeakThreadCount());
		message.setUserThreadCount(message.getThreadCount() - JVMThreadUtils.getDaemonThreadCount());
		message.setDeadLockedThreadCount(JVMThreadUtils.getDeadLockedThreadCount());
	}
 
}
