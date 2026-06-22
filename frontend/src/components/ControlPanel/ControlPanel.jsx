import { useState } from 'react';
import styles from './ControlPanel.module.css';

export default function ControlPanel({
  rows, // acts as size since rows === cols
  onResize,
  onClearAll,
  onClearPath,
  onGenerateRandomWalls,
  onRun,
  isVisualizing,
  speed,
  onSpeedChange,
  algorithm,
  onAlgorithmChange
}) {
  const [size, setSize] = useState(rows);

  const handleSliderChange = (e) => {
    const val = Number(e.target.value);
    setSize(val);
    onResize(val, val);
  };

  return (
    <div className={`${styles.container} glass-panel`}>
      {/* Section: Algorithm Selector */}
      <div className={styles.section}>
        <h4 className={styles.sectionTitle}>Algorithm</h4>
        <div className={styles.algoGroup}>
          {['BFS', 'DFS', 'A*'].map((algo) => (
            <button
              key={algo}
              onClick={() => onAlgorithmChange(algo)}
              disabled={isVisualizing}
              className={`${styles.algoBtn} ${algorithm === algo ? styles.algoBtnActive : ''}`}
            >
              {algo}
            </button>
          ))}
        </div>
      </div>

      {/* Section 1: Execution */}
      <div className={styles.section}>
        <h4 className={styles.sectionTitle}>Execution</h4>
        <div className={styles.actionGroup}>
          <button
            onClick={onRun}
            className={`${styles.btn} ${styles.btnPrimary} ${isVisualizing ? styles.btnVisualizing : ''}`}
            disabled={isVisualizing}
          >
            {isVisualizing ? (
              <span className={styles.runningContainer}>
                <span className={styles.spinner}></span> Visualizing...
              </span>
            ) : (
              `Run ${algorithm} Pathfinder`
            )}
          </button>
          <div className={styles.rowGroup}>
            <button
              onClick={onClearPath}
              className={`${styles.btn} ${styles.btnSecondary}`}
              disabled={isVisualizing}
            >
              Clear Path
            </button>
            <button
              onClick={onClearAll}
              className={`${styles.btn} ${styles.btnDanger}`}
              disabled={isVisualizing}
            >
              Clear All
            </button>
          </div>
        </div>
      </div>

      {/* Section 2: Grid Size Options */}
      <div className={styles.section}>
        <h4 className={styles.sectionTitle}>Grid Size ({size} × {size})</h4>
        <div className={styles.sizeControl}>
          <input
            type="range"
            min="4"
            max="30"
            step="1"
            value={size}
            onChange={handleSliderChange}
            disabled={isVisualizing}
            className={styles.rangeInput}
          />
        </div>

        <div className={styles.presets}>
          <span className={styles.presetsLabel}>Presets:</span>
          {[10, 15, 20, 25, 30].map((s) => (
            <button
              key={s}
              onClick={() => onResize(s, s)}
              disabled={isVisualizing || rows === s}
              className={`${styles.presetBtn} ${rows === s ? styles.presetBtnActive : ''}`}
            >
              {s}×{s}
            </button>
          ))}
        </div>
      </div>

      {/* Section 3: Settings */}
      <div className={styles.section}>
        <h4 className={styles.sectionTitle}>Settings</h4>
        <div className={styles.settingsGroup}>
          <button
            onClick={onGenerateRandomWalls}
            className={`${styles.btn} ${styles.btnGenerator}`}
            disabled={isVisualizing}
          >
            Generate Random Walls
          </button>

          <div className={styles.speedControl}>
            <div className={styles.speedLabels}>
              <span className={styles.inputLabel}>Animation Speed</span>
              <span className={styles.speedValue}>
                {speed <= 20 ? 'Fast' : speed <= 80 ? 'Medium' : 'Slow'}
              </span>
            </div>
            <input
              type="range"
              min="5"
              max="200"
              step="5"
              value={speed}
              onChange={(e) => onSpeedChange(Number(e.target.value))}
              disabled={isVisualizing}
              className={styles.rangeInput}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
