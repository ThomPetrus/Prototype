import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

const AssetView = () => {

  const [asset, setAsset] = useState<string>('');
  const navigate = useNavigate();
  const { fileName } = useParams();

  const getData = async () => {
    fetch(`/assets/${fileName}`)
      .then((response) => response.blob())
      .then((response) => {
        setAsset(URL.createObjectURL(response));
      })
      .catch(err => {
        console.log(err);
      });
  }

  useEffect(() => {
    getData();
  }, []);

  const title = <h2>{asset ? 'View Asset' : 'Asset Not Found'}</h2>;

  return (
    <div>
      <AppNavbar />
      <Container>
        {title}
        insert three.js here
      </Container>
    </div>
  )
};

export default AssetView;