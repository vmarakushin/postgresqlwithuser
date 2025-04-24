
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $scriptDir

docker build -t my-postgres .

docker run -d --name pg-container -p 127.0.0.1:12557:12557 my-postgres

