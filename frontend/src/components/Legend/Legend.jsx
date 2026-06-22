import styles from './Legend.module.css';

export default function Legend() {
  const items = [
    { label: 'Start Point', className: styles.start },
    { label: 'End Point', className: styles.end },
    { label: 'Walkable Cell', className: styles.walkable },
    { label: 'Wall / Obstacle', className: styles.wall },
    { label: 'Visited Cell', className: styles.visited },
    { label: 'Shortest Path', className: styles.path },
  ];

  return (
    <div className={`${styles.legend} glass-panel`}>
      {items.map((item, index) => (
        <div key={index} className={styles.item}>
          <div className={`${styles.box} ${item.className}`}>
            {item.label === 'Start Point' && 'S'}
            {item.label === 'End Point' && 'E'}
          </div>
          <span className={styles.label}>{item.label}</span>
        </div>
      ))}
    </div>
  );
}
