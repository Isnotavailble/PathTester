import { useRef } from 'react';
import styles from './MatrixGrid.module.css';

export default function MatrixGrid({
  matrix,
  startPoint,
  endPoint,
  visitedCells,
  pathCells,
  isVisualizing,
  onCellToggleWall,
  onMoveStart,
  onMoveEnd
}) {
  const isMouseDownRef = useRef(false);
  const dragTypeRef = useRef(null); // 'start' | 'end' | 'wall' | 'eraser' | null

  const handleMouseDown = (row, col) => {
    if (isVisualizing) return;
    
    isMouseDownRef.current = true;
    
    const isStart = row === startPoint.row && col === startPoint.column;
    const isEnd = row === endPoint.row && col === endPoint.column;
    
    if (isStart) {
      dragTypeRef.current = 'start';
    } else if (isEnd) {
      dragTypeRef.current = 'end';
    } else {
      const isWall = matrix[row][col] === 0;
      if (isWall) {
        dragTypeRef.current = 'eraser';
        onCellToggleWall(row, col, 1); // convert to walkable
      } else {
        dragTypeRef.current = 'wall';
        onCellToggleWall(row, col, 0); // convert to wall
      }
    }
  };

  const handleMouseEnter = (row, col) => {
    if (!isMouseDownRef.current || isVisualizing) return;
    
    const type = dragTypeRef.current;
    const isStart = row === startPoint.row && col === startPoint.column;
    const isEnd = row === endPoint.row && col === endPoint.column;

    if (type === 'start') {
      if (!isEnd) {
        onMoveStart(row, col);
      }
    } else if (type === 'end') {
      if (!isStart) {
        onMoveEnd(row, col);
      }
    } else if (type === 'wall') {
      if (!isStart && !isEnd) {
        onCellToggleWall(row, col, 0);
      }
    } else if (type === 'eraser') {
      if (!isStart && !isEnd) {
        onCellToggleWall(row, col, 1);
      }
    }
  };

  const handleMouseUpOrLeave = () => {
    isMouseDownRef.current = false;
    dragTypeRef.current = null;
  };

  const rows = matrix.length;
  const cols = matrix[0]?.length || 0;

  // Render cells
  return (
    <div 
      className={`${styles.gridContainer} glass-panel`}
      onMouseLeave={handleMouseUpOrLeave}
      onMouseUp={handleMouseUpOrLeave}
    >
      <div 
        className={styles.grid}
        style={{
          gridTemplateRows: `repeat(${rows}, minmax(0, 1fr))`,
          gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`,
          aspectRatio: `${cols} / ${rows}`
        }}
      >
        {matrix.map((rowArr, r) => 
          rowArr.map((cellValue, c) => {
            const isStart = r === startPoint.row && c === startPoint.column;
            const isEnd = r === endPoint.row && c === endPoint.column;
            const isWall = cellValue === 0;
            const isPath = pathCells.some(p => p.row === r && p.column === c);
            const isVisited = visitedCells.some(p => p.row === r && p.column === c);

            // Determine classes
            let cellClass = styles.cell;
            if (isStart) {
              cellClass += ` ${styles.cellStart}`;
            } else if (isEnd) {
              cellClass += ` ${styles.cellEnd}`;
            } else if (isWall) {
              cellClass += ` ${styles.cellWall}`;
            } else {
              cellClass += ` ${styles.cellWalkable}`;
            }

            // Path & visited animations (only if not start/end)
            if (!isStart && !isEnd) {
              if (isPath) {
                cellClass += ` ${styles.cellPath}`;
              } else if (isVisited) {
                cellClass += ` ${styles.cellVisited}`;
              }
            }

            return (
              <div
                key={`${r}-${c}`}
                className={cellClass}
                onMouseDown={() => handleMouseDown(r, c)}
                onMouseEnter={() => handleMouseEnter(r, c)}
                onTouchStart={(e) => {
                  // Prevent scrolling while interacting with the grid on mobile
                  e.preventDefault();
                  handleMouseDown(r, c);
                }}
              >
                {isStart && <span className={styles.markerText}>S</span>}
                {isEnd && <span className={styles.markerText}>E</span>}
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}
