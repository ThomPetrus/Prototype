import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

const FileNameList = () => {

  const [files, setFileNames] = useState([{
    fileName: "",
  }]);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetch('assets/filenames')
      .then(response => response.json())
      .then(data => {
        setFileNames(data);
        setLoading(false);
      })
  }, []);


  if (loading) {
    return <p>Loading...</p>;
  }

  const fileNameList = files.map(file => {
    return <tr key={file.fileName}>
      <td style={{whiteSpace: 'nowrap'}}>{file.fileName}</td>
      <td>
        <ButtonGroup>
            <Button color="link"><Link to={`/${file.fileName}`}>View</Link></Button>
        </ButtonGroup>
      </td>
    </tr>
  });

  return (
    <div>
      <AppNavbar/>
      <Container fluid>
        <h3>Sample Assets</h3>
        <Table className="mt-4">
          <thead>
          <tr>
            <th>Name</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          {fileNameList}
          </tbody>
        </Table>
      </Container>
    </div>
  );
};

export default FileNameList;