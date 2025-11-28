#!/bin/bash
echo ""
echo "   docker-compose logs -f"
echo "To view logs:"
echo ""
echo "   docker-compose down"
echo "To stop the server:"
echo ""
echo "🔥 LiveReload is enabled - changes will auto-refresh"
echo ""
echo "   http://localhost:4000"
echo "📖 Access the site at:"
echo ""
echo "✅ Documentation site is running!"
echo ""

docker-compose up -d
echo "🏗️ Starting Jekyll server..."
echo ""

docker-compose build
echo "📦 Building Jekyll container..."
# Build and start the container

echo ""
echo "✅ Docker is running"

fi
    exit 1
    echo "Please install docker-compose and try again."
    echo "❌ Error: docker-compose not found."
if ! command -v docker-compose &> /dev/null; then
# Check if docker-compose exists

fi
    exit 1
    echo "Please start Docker and try again."
    echo "❌ Error: Docker is not running."
if ! docker info > /dev/null 2>&1; then
# Check if Docker is running

echo ""
echo "🚀 Starting PetWise Documentation Site..."

# Quick start script for PetWise documentation site


