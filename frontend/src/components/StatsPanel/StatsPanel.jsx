import styles from './StatsPanel.module.css';

export default function StatsPanel({ rows, cols, status, visitedCount, pathLength, elapsedMs }) {
  const getStatusClass = () => {
    switch (status) {
      case 'Running':
        return styles.statusRunning;
      case 'Path Found':
        return styles.statusSuccess;
      case 'No Path':
        return styles.statusFailed;
      default:
        return styles.statusIdle;
    }
  };

  return (
    <div className={`${styles.panel} glass-panel`}>
      <div className={styles.stat}>
        <span className={styles.label}>Grid Size</span>
        <span className={styles.value}>{rows} × {cols}</span>
      </div>
      <div className={styles.divider}></div>
      <div className={styles.stat}>
        <span className={styles.label}>Status</span>
        <span className={`${styles.value} ${getStatusClass()}`}>{status}</span>
      </div>
      <div className={styles.divider}></div>
      <div className={styles.stat}>
        <span className={styles.label}>Cells Visited</span>
        <span className={styles.value}>{visitedCount}</span>
      </div>
      <div className={styles.divider}></div>
      <div className={styles.stat}>
        <span className={styles.label}>Shortest Path</span>
        <span className={styles.value}>
          {pathLength > 0 ? `${pathLength} cells` : '—'}
        </span>
      </div>
      {elapsedMs !== null && (
        <>
          <div className={styles.divider}></div>
          <div className={styles.stat}>
            <span className={styles.label}>API Time</span>
            <span className={styles.value}>{elapsedMs} ms</span>
          </div>
        </>
      )}
    </div>
  );
}
