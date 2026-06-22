const API_URL = 'http://localhost:8080/api/bfs';

/**
 * Sends a grid matrix, start point, and end point to the BFS endpoint
 * and returns the traversal order and shortest path.
 * 
 * @param {number[][]} matrix - 2D grid matrix of 0s (walls) and 1s (walkable)
 * @param {{row: number, column: number}} start - Start coordinate
 * @param {{row: number, column: number}} end - End coordinate
 * @returns {Promise<{path: Array<{row: number, column: number}>, traversalOrder: Array<{row: number, column: number}>}>}
 */
export async function fetchBfsPath(matrix, start, end) {
  const requestBody = {
    matrix,
    start: {
      row: start.row,
      column: start.column
    },
    end: {
      row: end.row,
      column: end.column
    }
  };

  const startTime = performance.now();

  try {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Server responded with ${response.status}: ${errorText || 'Unknown error'}`);
    }

    const data = await response.json();
    
    return {
      path: data.path || [],
      traversalOrder: data.traversalOrder || [],
      elapsedMs: data.timeTaken !== undefined ? data.timeTaken : 0
    };
  } catch (error) {
    console.error('Error fetching path from BFS API:', error);
    throw error;
  }
}
