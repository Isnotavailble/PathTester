import { useState, useRef, useEffect } from 'react';
import Header from './components/Header/Header';
import Legend from './components/Legend/Legend';
import StatsPanel from './components/StatsPanel/StatsPanel';
import ControlPanel from './components/ControlPanel/ControlPanel';
import MatrixGrid from './components/MatrixGrid/MatrixGrid';
import Alert from './components/Alert/Alert';
import { fetchBfsPath } from './api/bfsService';
import { fetchDfsPath } from './api/dfsService';
import { fetchAstarPath } from './api/astarService';
import './App.css';

const createInitialMatrix = (r, c) => {
  return Array.from({ length: r }, () => Array(c).fill(1));
};

function App() {
  const [rows, setRows] = useState(10);
  const [cols, setCols] = useState(10);
  const [matrix, setMatrix] = useState(() => createInitialMatrix(10, 10));
  const [startPoint, setStartPoint] = useState({ row: 0, column: 0 });
  const [endPoint, setEndPoint] = useState({ row: 9, column: 9 });
  const [algorithm, setAlgorithm] = useState('BFS');

  const [isVisualizing, setIsVisualizing] = useState(false);
  const [visitedCells, setVisitedCells] = useState([]);
  const [pathCells, setPathCells] = useState([]);
  const [animationSpeed, setAnimationSpeed] = useState(30); // ms
  
  const [status, setStatus] = useState('Idle');
  const [elapsedMs, setElapsedMs] = useState(null);
  const [alert, setAlert] = useState(null); // { message, type }

  const activeTimersRef = useRef([]);

  const clearTimers = () => {
    activeTimersRef.current.forEach(clearInterval);
    activeTimersRef.current = [];
  };

  useEffect(() => {
    return () => clearTimers();
  }, []);

  const handleResize = (newRows, newCols) => {
    clearTimers();
    setRows(newRows);
    setCols(newCols);
    setMatrix(createInitialMatrix(newRows, newCols));
    
    // Set smart positions for start and end
    setStartPoint({ row: 0, column: 0 });
    setEndPoint({ row: newRows - 1, column: newCols - 1 });
    
    setVisitedCells([]);
    setPathCells([]);
    setStatus('Idle');
    setElapsedMs(null);
    setAlert(null);
  };

  const handleCellToggleWall = (r, c, value) => {
    setMatrix(prev => {
      const copy = prev.map((rowArr, rowIndex) => 
        rowArr.map((val, colIndex) => {
          if (rowIndex === r && colIndex === c) return value;
          return val;
        })
      );
      return copy;
    });
  };

  const handleMoveStart = (r, c) => {
    // If start is moved onto a wall, automatically clear it to walkable (1)
    if (matrix[r][c] === 0) {
      handleCellToggleWall(r, c, 1);
    }
    setStartPoint({ row: r, column: c });
  };

  const handleMoveEnd = (r, c) => {
    // If end is moved onto a wall, automatically clear it to walkable (1)
    if (matrix[r][c] === 0) {
      handleCellToggleWall(r, c, 1);
    }
    setEndPoint({ row: r, column: c });
  };

  const handleClearPath = () => {
    clearTimers();
    setVisitedCells([]);
    setPathCells([]);
    setStatus('Idle');
    setElapsedMs(null);
    if (alert && alert.type === 'info') {
      setAlert(null);
    }
  };

  const handleClearAll = () => {
    clearTimers();
    setMatrix(createInitialMatrix(rows, cols));
    setVisitedCells([]);
    setPathCells([]);
    setStatus('Idle');
    setElapsedMs(null);
    setAlert(null);
  };

  const handleGenerateRandomWalls = () => {
    clearTimers();
    setVisitedCells([]);
    setPathCells([]);
    setStatus('Idle');
    setElapsedMs(null);
    setAlert(null);

    setMatrix(prev => {
      return prev.map((rowArr, r) => 
        rowArr.map((val, c) => {
          // Do not overwrite start/end points
          const isStart = r === startPoint.row && c === startPoint.column;
          const isEnd = r === endPoint.row && c === endPoint.column;
          if (isStart || isEnd) return 1;
          
          // ~28% chance of wall
          return Math.random() < 0.28 ? 0 : 1;
        })
      );
    });
  };

  const runVisualizer = async () => {
    if (isVisualizing) return;
    
    // Clear path but keep walls
    handleClearPath();
    setIsVisualizing(true);
    setStatus('Running');

    try {
      let result;
      if (algorithm === 'BFS') {
        result = await fetchBfsPath(matrix, startPoint, endPoint);
      } else if (algorithm === 'DFS') {
        result = await fetchDfsPath(matrix, startPoint, endPoint);
      } else if (algorithm === 'A*') {
        result = await fetchAstarPath(matrix, startPoint, endPoint);
      }

      const { path, traversalOrder, elapsedMs } = result;
      setElapsedMs(elapsedMs);

      if (!traversalOrder || traversalOrder.length === 0) {
        setStatus('No Path');
        setIsVisualizing(false);
        setAlert({
          message: 'No path found! The end point is completely blocked by walls.',
          type: 'info'
        });
        return;
      }

      // Start visited cell animations
      animateBfs(traversalOrder, path);

    } catch (error) {
      console.error(error);
      setIsVisualizing(false);
      setStatus('Idle');
      setAlert({
        message: `Could not connect to the ${algorithm} API. Please ensure the backend server is running on http://localhost:8080.`,
        type: 'error'
      });
    }
  };

  const animateBfs = (traversalOrder, path) => {
    let index = 0;
    
    const interval = setInterval(() => {
      if (index < traversalOrder.length) {
        const cell = traversalOrder[index];
        setVisitedCells(prev => [...prev, cell]);
        index++;
      } else {
        clearInterval(interval);
        // Completed traversal path, now animate shortest path
        animateShortestPath(path);
      }
    }, animationSpeed);

    activeTimersRef.current.push(interval);
  };

  const animateShortestPath = (path) => {
    if (!path || path.length === 0) {
      setStatus('No Path');
      setIsVisualizing(false);
      setAlert({
        message: 'No path exists between start and end points.',
        type: 'info'
      });
      return;
    }

    let index = 0;
    
    const interval = setInterval(() => {
      if (index < path.length) {
        const cell = path[index];
        setPathCells(prev => [...prev, cell]);
        index++;
      } else {
        clearInterval(interval);
        setStatus('Path Found');
        setIsVisualizing(false);
      }
    }, animationSpeed * 1.5); // animate shortest path slightly slower for dramatic effect

    activeTimersRef.current.push(interval);
  };

  return (
    <div className="appContainer">
      <Header />
      <div className="contentWrapper">
        <StatsPanel
          rows={rows}
          cols={cols}
          status={status}
          visitedCount={visitedCells.length}
          pathLength={pathCells.length}
          elapsedMs={elapsedMs}
        />

        {alert && (
          <Alert
            message={alert.message}
            type={alert.type}
            onClose={() => setAlert(null)}
          />
        )}

        <div className="layoutRow">
          <div className="layoutColumnLeft">
            <MatrixGrid
              matrix={matrix}
              startPoint={startPoint}
              endPoint={endPoint}
              visitedCells={visitedCells}
              pathCells={pathCells}
              isVisualizing={isVisualizing}
              onCellToggleWall={handleCellToggleWall}
              onMoveStart={handleMoveStart}
              onMoveEnd={handleMoveEnd}
            />
            <Legend />
          </div>

          <div className="layoutColumnRight">
            <ControlPanel
              key={`${rows}-${cols}`}
              rows={rows}
              cols={cols}
              algorithm={algorithm}
              onAlgorithmChange={setAlgorithm}
              onResize={handleResize}
              onClearAll={handleClearAll}
              onClearPath={handleClearPath}
              onGenerateRandomWalls={handleGenerateRandomWalls}
              onRun={runVisualizer}
              isVisualizing={isVisualizing}
              speed={animationSpeed}
              onSpeedChange={setAnimationSpeed}
            />
          </div>
        </div>
      </div>

      <footer className="footer">
        <p>Built with <span className="footerHighlight">React 19</span> & <span className="footerHighlight">Vite</span>. API Layer connected to <span className="footerHighlight">Spring Boot Pathfinding APIs</span>.</p>
      </footer>
    </div>
  );
}

export default App;
