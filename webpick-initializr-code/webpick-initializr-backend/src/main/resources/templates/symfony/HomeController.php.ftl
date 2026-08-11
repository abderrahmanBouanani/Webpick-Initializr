<?php

namespace ${namespace}\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Annotation\Route;

class HomeController extends AbstractController
{
    #[Route('/api', name: 'api_home')]
    public function index(): JsonResponse
    {
        return new JsonResponse([
            'project' => '${projectName}',
            'status' => 'online',
            'database' => '${database}',
            'namespace' => '${namespace}',
            'dependencies' => [
                <#if dependencies??>
                <#list dependencies as dep>
                '${dep}'<#if dep_has_next>,</#if>
                </#list>
                </#if>
            ]
        ]);
    }
}
