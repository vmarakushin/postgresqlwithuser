
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $scriptDir

docker build -t my-postgres .

docker run -d --name pg-container -p 127.0.0.1:5432:5432 my-postgres

