import React from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import FileNameList from './FileNameList';
import AssetView from './AssetView';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path='/files' element={<FileNameList/>}/>
        <Route path='/:fileName' element={<AssetView/>}/>
      </Routes>
    </Router>
  )
}

export default App;