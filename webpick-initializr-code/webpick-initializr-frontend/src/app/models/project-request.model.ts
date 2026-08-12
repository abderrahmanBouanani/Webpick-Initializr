export interface ProjectRequest {
  projectName: string;
  basePackage: string;
  includeGit: boolean;
  gitToken: string;
  backend: string;
  frontend: string;
  db: string;
  deps: string[];
  devops: string[];
  metadata: { [key: string]: string };
}
